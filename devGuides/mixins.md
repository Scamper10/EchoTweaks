[Examples](https://wiki.fabricmc.net/tutorial:mixin_examples) |
[Partial Documentation](https://github.com/2xsaiko/mixin-cheatsheet/tree/master) |
[Descriptor Types](https://lambdaurora.dev/tutorials/java/bytecode/types.html)

---

# Adding a Mixin

### <span style="color:pink">1.</span> Create your mixin class in java/mixin
\- It should be `abstract` and decorated `@Mixin(TargetClass.class)`<br>
\- It should also extend the superclass of the Target Class.

### <span style="color:pink">2.</span> Add your class name to resources/mixinInfo/general.json<br>
Or otherwise, if it's in a different package

### <span style="color:pink">3.</span> Write your mixin body

The name of the main method is never used and hence does not matter. It should be decorated for its purpose (like `@Inject(...)`, `@ModifyVariable(...)`, etc)

Fields and Methods that are from the target class are indicated under the mixin class decorated `@Shadow`. Ideally, method shadows are `abstract`, but `private` ones need a dummy method body.

Any new methods or fields you create should be decorated `@Unique` in order to avoid name conflicts when they are appended onto the target class.
