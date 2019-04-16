# RevolutTest revision 2
I tried to experiment with MVVM and android data-bindins.
Pros:
  - less classes (no View and presenter interfaces)
  - easier to write test on view model (no need to mock view and check if view method was called)
  - looks good for easy bindings and cases
Cons:
  - ugly onPropertyChanged callbacks which should be removed on destruction (could be limited by custom binding adapters)
  - xml layout could be large and ugly
  - still had to use text watchers, focus listeners and etc in some cases
  
  

