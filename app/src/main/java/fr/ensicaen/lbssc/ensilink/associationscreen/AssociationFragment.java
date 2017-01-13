package fr.ensicaen.lbssc.ensilink.associationscreen;


import android.os.Bundle;
import android.support.v4.app.ListFragment;

import fr.ensicaen.lbssc.ensilink.Updatable;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public abstract class AssociationFragment extends ListFragment implements Updatable {

    private Union _union;
    private int _unionId;

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

    protected Union getUnion(){
        return _union;
    }

    protected int getUnionId(){
        return _unionId;
    }

    public void changeUnion(int unionId){
        _unionId = unionId;
        _union = School.getInstance().getUnion(unionId);
        update();
    }
}
