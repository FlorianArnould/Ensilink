package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * Interface of a News that represent a modification
 */
public abstract class News {

    private final String _clubName;
    private final int _unionIndex;

    News(int unionId, String clubName) {
        _clubName = clubName;
        _unionIndex = unionId-1;
    }

    /**
     * Create the string to show in the notification
     * @return the string
     */
    public abstract String toNotificationString();

    /**
     * @return the name of the club which was updated
     */
    public String getClubName() {
        return _clubName;
    }

    /**
     * @return the index of the union of the club
     */
    public int getUnionIndex() {
        return _unionIndex;
    }

    /**
     * @return the index of the club
     */
    public int getClubIndex() {
        List<Club> list = School.getInstance().getUnion(_unionIndex).getClubs();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getName().equals(_clubName)){
                return i;
            }
        }
        return 0;
    }
}
