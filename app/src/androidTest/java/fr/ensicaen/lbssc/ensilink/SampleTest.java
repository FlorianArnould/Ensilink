package fr.ensicaen.lbssc.ensilink;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.ensicaen.lbssc.ensilink.utils.ColorCreator;

/**
 * Created by florian on 07/06/17.
 */
@RunWith(AndroidJUnit4.class)
public class SampleTest {
	@Test
	public void test(){
		ColorCreator.darkerColor(Color.BLUE);
	}
}
