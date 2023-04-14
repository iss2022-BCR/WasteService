package it.unibo.radarSystem22.domainBCR.models;

import it.unibo.radarSystem22.domainBCR.concrete.BuzzerConcrete;
import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.mock.BuzzerMockCLI;
import it.unibo.radarSystem22.domainBCR.mock.BuzzerMockSound;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class BuzzerModel implements IBuzzer {
    private BuzzerState state = BuzzerState.OFF;

    public static IBuzzer create()
    {
        return DomainSystemConfig.simulation ?
                createBuzzerMock() : createBuzzerConcrete();
    }

    public static IBuzzer createBuzzerMock()
    {
        return DomainSystemConfig.buzzerSound ?
                BuzzerMockSound.createBuzzer() : new BuzzerMockCLI();
    }

    public static IBuzzer createBuzzerConcrete()
    {
        ColorsOut.out("createBuzzerConcrete", ColorsOut.BLUE);
        return new BuzzerConcrete();
    }
    
    protected abstract void buzzerActivate(BuzzerState val);
    
    protected void setState(BuzzerState val)
    {
        state = val;
        buzzerActivate(state);
    }
    
    @Override
    public void turnOn()
    {
        setState(BuzzerState.ON);
    }
    
    @Override
    public void turnOff()
    {
        setState(BuzzerState.OFF);
    }
    
    @Override
    public void turnIntermittent()
    {
        setState(BuzzerState.INTERMITTENT);
    }

    @Override
    public BuzzerState getState()
    {
        return state;
    }
}
