%====================================================================================
% test_interrupt description   
%====================================================================================
context(ctx_interrupt_test, "localhost",  "TCP", "11800").
 qactor( user, ctx_interrupt_test, "it.unibo.user.User").
  qactor( interruptcaller, ctx_interrupt_test, "it.unibo.interruptcaller.Interruptcaller").
  qactor( test, ctx_interrupt_test, "it.unibo.test.Test").
