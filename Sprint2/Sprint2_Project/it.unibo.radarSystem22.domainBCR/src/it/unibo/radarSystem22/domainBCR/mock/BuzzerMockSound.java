package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class BuzzerMockSound extends BuzzerMockCLI {
    private Clip clip;

    private Thread intermittenceProvider;

    public BuzzerMockSound()
    {
        super();
        configure();
    }

    protected void configure()
    {
        try {
            URL url = new URL("https://www.soundjay.com/buttons/beep-09.wav");
            clip = AudioSystem.getClip();

            // getAudioInputStream() also accepts a File or InputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IBuzzer createBuzzer()
    {
        return new BuzzerMockSound();
    }

    @Override
    public void turnOn()
    {
        if(clip.isActive())
            clip.stop();

        super.turnOn();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void turnOff()
    {
        if(clip.isActive())
            clip.stop();

        super.turnOff();
    }

    @Override
    public void turnIntermittent()
    {
        if(clip.isActive())
            clip.stop();

        super.turnIntermittent();
        intermittenceProvider = new Thread(new Runnable() {
            private boolean isOn = false;
            @Override
            public void run() {
                while(getState().equals(BuzzerState.INTERMITTENT))
                {
                    isOn = !isOn;

                    if(isOn)
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    else
                        clip.stop();

                    BasicUtils.delay(DomainSystemConfig.buzzerDelay);
                }
            }
        });

        intermittenceProvider.start();
    }
}
