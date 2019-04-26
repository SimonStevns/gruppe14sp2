package Domain;

import java.util.HashMap;

public class Privileges {

    private final HashMap<Privilege, Boolean> privileges = new HashMap<>();

    public Privileges(boolean[] bolPrivArray) {

        if (bolPrivArray.length == Privilege.values().length) {

            Privilege[] privArray = Privilege.values();

            for (int i = 0; i < bolPrivArray.length; i++) {
                privileges.put(privArray[i], bolPrivArray[i]);
            }
        }
    }

    public boolean hasPrivlege(Privilege priv) {
        return privileges.get(priv);
    }

}
