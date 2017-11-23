
package si.fri.rso.upravljanjeprofilov;

import java.util.ArrayList;
import java.util.List;


public class Database {
    private static List<Profil> profils = new ArrayList<>();

    public static List<Profil> getProfils() {
        /*Profil cus = new Profil();
        cus.setId("1");
        cus.setFirstName("Janez");
        cus.setLastName("Novak");
        profils.add(cus);*/

        return profils;
    }

    public static Profil getProfil(String profilId) {
        for (Profil profil : profils) {
            if (profil.getId().equals(profilId))
                return profil;
        }

        return null;
    }

    public static void addProfil(Profil profil) {
        profils.add(profil);
    }

    public static void deleteProfil(String profilId) {
        for (Profil profil : profils) {
            if (profil.getId().equals(profilId)) {
                profils.remove(profil);
                break;
            }
        }
    }
}
