**javax.lang.model.element.Element**<br/>
**#getEnclosingElement()** (Java)<br/>
**#enclosingElement** (Kotlin)<br/>

Ví dụ 1: có một TypeElement như "abc.library.Test" thì function này trả về 
một package như "abc.library".<br/>

Ví dụ 2: có một TypeElement như "abc.library.Test.Temp" thì function này trả về 
một TypeElement như "abc.library.Test".<br/>

