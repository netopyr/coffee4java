@global = @



@test = (msg, fn) ->
  desc = org.junit.runner.Description.createTestDescription(scriptName, msg)
  scriptDescription.addChild(desc)
  junitNotifier.fireTestStarted(desc)
  try
    fn()
    junitNotifier.fireTestFinished(desc)
  catch msg
    junitNotifier.fireTestFailure(new org.junit.runner.notification.Failure(desc, new java.lang.AssertionError(msg)));




@ok = (good, msg) ->
  if !good then throw msg



@eq = (x, y, msg) -> ok x is y, msg ? x + ' !== ' + y


@doesNotThrow = (fn) ->
  fn()
  ok true



@arrayEqual = (a, b) ->
  if a is b
    # 0 isnt -0
    a isnt 0 or 1/a is 1/b
  else if a instanceof Array and b instanceof Array
    return no unless a.length is b.length
    return no for el, idx in a when not arrayEqual el, b[idx]
    return yes
  else
    # NaN is NaN
    a isnt a and b isnt b

@arrayEq = (a, b, msg) -> ok arrayEqual(a,b), msg ? a + ' !== ' + b



@throws = (fun, err, msg) ->
  try
    fun()
  catch e
    if err
      eq e, err, msg
    else
      ok yes
    return
  ok no, msg



@fail = () ->
  ok no
