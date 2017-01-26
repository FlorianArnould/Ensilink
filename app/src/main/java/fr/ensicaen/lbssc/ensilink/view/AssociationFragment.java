package fr.ensicaen.lbssc.ensilink.view;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Abstraction to store the union of the fragment
 */
public abstract class AssociationFragment extends ListFragment implements Updatable {

    private Union _union;
    private int _unionId;

    /**
     * Method to use to initialize an AssociationFragment
     * @param unionId the id of the union displayed in the fragment
     * @param fragment the fragment to initialize
     */
    protected static void newInstance(int unionId, AssociationFragment fragment){
        Bundle args = new Bundle();
        args.putInt("UNION_ID", unionId);
        fragment.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _unionId = getArguments().getInt("UNION_ID");
            _union = School.getInstance().getUnion(_unionId);

        }
    }

    /**
     * @return the union displayed in the fragment
     */
    protected Union getUnion(){
        return _union;
    }

    /**
     * @return the id of the union displayed in the fragment
     */
    protected int getUnionId(){
        return _unionId;
    }

    /**
     * change the union displayed in the fragment by the union which has this ID
     * @param unionId the id of the new union
     */
    public void changeUnion(int unionId){
        _unionId = unionId;
        _union = School.getInstance().getUnion(unionId);
        update();
    }
}