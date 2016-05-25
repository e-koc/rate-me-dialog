package net.emrekoc.ratemedialogsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import net.emrekoc.ratemedialog.RateMe;

public class SampleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    RateMe.register(this, 1);

    RateMe.showImmediately(this);
  }
}
