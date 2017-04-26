/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

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
