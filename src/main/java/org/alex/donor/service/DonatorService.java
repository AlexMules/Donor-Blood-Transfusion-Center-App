package org.alex.donor.service;

import lombok.RequiredArgsConstructor;
import org.alex.donor.model.*;
import org.alex.donor.model.enums.*;
import org.alex.donor.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import com.lowagie.text.pdf.BaseFont;

@Service
@RequiredArgsConstructor
public class DonatorService {

    private final UtilizatorRepository utilizatorRepo;
    private final AdresaRepository adresaRepo;
    private final DonatorRepository donatorRepo;
    private final PasswordEncoder passwordEncoder;
    private final AnalizaSangeRepository analizaRepo;
    private final AlertaRepository alertaRepo;
    private final DonareRepository donareRepo;
    private final AutentificareService autentificareService;

    @Transactional
    public void inregistrareDonator(Utilizator u, Adresa a, Donator d) {
        // validari unicitate donator
        if (utilizatorRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Există deja un cont cu această adresă de e-mail!");
        }

        if (donatorRepo.existsByCnp(d.getCnp())) {
            throw new RuntimeException("Acest CNP este deja înregistrat!");
        }

        // procesare utilizator
        u.setParola(passwordEncoder.encode(u.getParola())); // criptare parola
        u.setRol(Rol.DONATOR);
        Utilizator utilizatorSalvat = utilizatorRepo.save(u);

        // procesare adresa
        Adresa adresaSalvata = adresaRepo.save(a);

        // procesare donator
        d.setUtilizator(utilizatorSalvat);
        d.setAdresa(adresaSalvata);
        d.setStatus(StatusDonator.ELIGIBIL);
        d.setVarsta(Period.between(d.getDataNasterii().toLocalDate(), LocalDate.now()).getYears());

        // salvare finala
        donatorRepo.save(d);
    }

    public Donator getDonatorByUtilizator(Utilizator u) {
        return donatorRepo.findByUtilizator(u)
                .orElseThrow(() -> new RuntimeException("Profilul de donator nu a fost găsit!"));
    }

    public List<AnalizaSange> getIstoricAnalize(Donator donator) {
        return analizaRepo.findAllByDonator(donator);
    }

    public StatusDonator getStatusDonator(Donator donator) {
        return donator.getStatus();
    }

    public String getMesajStatus(Donator donator) {
        StatusDonator status = donator.getStatus();

        return switch (status) {
            case ELIGIBIL -> "Ești eligibil pentru a dona! Te poți programa oricând.";
            case INELIGIBIL_TEMPORAR -> "Momentan nu poți dona (perioadă de recuperare/așteptarea rezultatului analizei).";
            case INELIGIBIL_PERMANENT -> "Din motive medicale, nu poți dona sânge!";
        };
    }

    public List<Alerta> getAlertePersonale() {
        // 1. Obținem utilizatorul logat
        Utilizator u = autentificareService.getUtilizatorLogat();
        if (u == null) {
            throw new RuntimeException("Nu sunteți autentificat!");
        }

        // 2. Găsim profilul de donator
        Donator donator = donatorRepo.findByUtilizator(u)
                .orElseThrow(() -> new RuntimeException("Profilul de donator nu a fost găsit!"));

        // 3. VERIFICARE ELIGIBILITATE
        // Dacă statusul nu este ELIGIBIL, returnăm o listă goală (donatorul nu vede alertele)
        if (donator.getStatus() != StatusDonator.ELIGIBIL) {
            return List.of();
        }

        // 4. Verificăm dacă are grupa de sânge stabilită
        if (donator.getGrupaSanguina() == null || donator.getRh() == null) {
            return List.of();
        }

        // 5. Returnăm alertele compatibile
        return alertaRepo.findAllByGrupaSanguinaAndRhOrderByDataOraDesc(
                donator.getGrupaSanguina(),
                donator.getRh()
        );
    }

    /**
     * Actualizează datele de acces ale donatorului după verificarea parolei actuale.
     */
    @Transactional
    public void actualizeazaContDonator(Integer id, String parolaActuala, String nouEmail, String nouaParola) {
        Utilizator user = utilizatorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        if (!passwordEncoder.matches(parolaActuala, user.getParola())) {
            throw new RuntimeException("Parola actuală introdusă este incorectă!");
        }

        if (nouEmail != null && !nouEmail.trim().isEmpty() && !nouEmail.equals(user.getEmail())) {
            if (utilizatorRepo.findByEmail(nouEmail).isPresent()) {
                throw new RuntimeException("Eroare: Email-ul " + nouEmail + " este deja utilizat!");
            }
            user.setEmail(nouEmail);
        }

        if (nouaParola != null && !nouaParola.trim().isEmpty()) {
            user.setParola(passwordEncoder.encode(nouaParola));
        }

        // 1. Salvăm în baza de date
        Utilizator userSalvat = utilizatorRepo.save(user);

        // 2. ACTUALIZĂM SESIUNEA (Sincronizăm RAM cu DB)
        autentificareService.refreshSesiune(userSalvat);
    }

    public byte[] genereazaCertificatComplet(Donator donator) {
        if (donator.getStatus() != StatusDonator.ELIGIBIL) {
            throw new RuntimeException("Certificatul este disponibil doar pentru donatorii eligibili.");
        }

        // Înregistrăm folderele de sistem pentru a găsi Arial
        FontFactory.registerDirectories();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // --- MODIFICARE FONTURI PENTRU DIACRITICE (Unicode Identity-H) ---
            // Folosim IDENTITY_H și EMBEDDED pentru a afișa corect ș și ț
            Font fontTitlu = FontFactory.getFont("Arial", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD);
            Font fontSectiune = FontFactory.getFont("Arial", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD);
            Font fontNormal = FontFactory.getFont("Arial", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL);
            // -----------------------------------------------------------------

            // 3. ADĂUGARE LOGO
            try {
                URL logoUrl = getClass().getResource("/images/logo.jpg");
                if (logoUrl != null) {
                    Image logo = Image.getInstance(logoUrl);
                    logo.scaleToFit(100, 100);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    document.add(logo);
                }
            } catch (Exception e) {
                System.err.println("Logoul nu a putut fi încărcat: " + e.getMessage());
            }

            // 4. TITLU
            Paragraph titlu = new Paragraph("CERTIFICAT DE DONATOR", fontTitlu);
            titlu.setAlignment(Element.ALIGN_CENTER);
            titlu.setSpacingAfter(25);
            document.add(titlu);

            // 5. DATE PERSONALE ȘI DE CONTACT (Folosește fontSectiune - Bold)
            Utilizator u = donator.getUtilizator();
            document.add(new Paragraph("DATE PERSONALE", fontSectiune));
            document.add(new Paragraph("Nume și Prenume: " + u.getNume() + " " + u.getPrenume(), fontNormal));
            document.add(new Paragraph("CNP: " + donator.getCnp(), fontNormal));

            DateTimeFormatter dtfSimplu = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String dataNastereStr = (donator.getDataNasterii() != null) ? donator.getDataNasterii().format(dtfSimplu) : "N/A";

            document.add(new Paragraph("Data nașterii: " + dataNastereStr, fontNormal));
            document.add(new Paragraph("Vârsta: " + donator.getVarsta() + " ani | Sex: " + donator.getSex(), fontNormal));
            document.add(new Paragraph("Email: " + u.getEmail() + " | Tel: " + u.getNrTelefon(), fontNormal));

            // 6. DATE DOMICILIU (Folosește fontSectiune - Bold)
            Adresa a = donator.getAdresa();
            document.add(new Paragraph("\nDOMICILIU", fontSectiune));
            if (a != null) {
                document.add(new Paragraph(String.format("Județ: %s, Localitate: %s", a.getJudet(), a.getLocalitate()), fontNormal));
                document.add(new Paragraph(String.format("Strada: %s, Nr. %d, Cod Poștal: %s",
                        a.getStrada() != null ? a.getStrada() : "-",
                        a.getNumar() != null ? a.getNumar() : 0,
                        a.getCodPostal() != null ? a.getCodPostal() : "-"), fontNormal));
            }

            // 7. DATE MEDICALE ȘI ISTORIC (Folosește fontSectiune - Bold)
            document.add(new Paragraph("\nINFORMAȚII MEDICALE", fontSectiune));
            document.add(new Paragraph("Grupa Sanguină: " + donator.getGrupaSanguina() + " | Rh: " + donator.getRh(), fontNormal));

            LocalDateTime prima = donareRepo.findDataPrimaDonareAdmisa(donator.getId());
            LocalDateTime ultima = donareRepo.findDataUltimaDonareAdmisa(donator.getId());

            document.add(new Paragraph("Data primei donări: " + (prima != null ? prima.format(dtfSimplu) : "N/A"), fontNormal));
            document.add(new Paragraph("Data ultimei donări: " + (ultima != null ? ultima.format(dtfSimplu) : "N/A"), fontNormal));

            // 8. FINALIZARE DOCUMENT
            Paragraph finalMesaj = new Paragraph("\n\nAcest certificat a fost eliberat la data de: " +
                    LocalDateTime.now().format(dtfSimplu), fontNormal);
            finalMesaj.setAlignment(Element.ALIGN_RIGHT);
            document.add(finalMesaj);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la generarea PDF-ului: " + e.getMessage());
        }
    }
}
