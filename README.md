# Lox
Another implementation of Lox programming language from the awesome book [craftinginterpreters](http://www.craftinginterpreters.com/) by [Bob Nystrom](https://github.com/munificent).

**Lox** is a dynamically typed programming language, its syntax is a member of the C family.

### Data types

* boolean `true; false;`
* numbers `19 // integer` `3.1415 // decimal`
* strings `"hello"`
* `nil`

### Expressions

#### Arithmetic
* `a + b`
* `a - b`
* `a * b`
* `a / b`
* `- negateMe`

#### Comparison and equality
* `a < b`
* `a <= b`
* `a > b`
* `a >= b`
* `a == b`
* `123 == 123 // true`
* `"123" == 123 // false`

#### Logical operator
* `and`
* `or`
* `!`

### Statements
* `print "hakuna matata"`
* `{
  print "a block of statements";
  print "hakuna matata btw";
  }`

### Variables
* `var name = "Clark";`
* `var lastname; // nil`

### Control Flow
```
if (somVariable == 5) {
  print "it's five!";
} else {
  print "it's not five.";
}
```

```
var a = 0;
while (a < 5) {
  print a;
  a = a + 1;
}
```

```
for (var i=0; i < 5; i = i +1) {
  print i;
}
```

### Functions
```
fun makeBreakfastFor(name) {
  print "Breakfast for " + name; 
} // it returns nil

fun sumPlease(a, b) {
  return a + b;
}
```
```
// functions are first-class in Lox
fun doSomeMath(a, b, op) {
  return op(a, b);
}
doSomeMath(sumPlease, 5, 6);
```

Lox also supports closures, local functions and block scope.

### Classes
```
class Breakfast {
  init(name) {
    this.name = name;
  }
  cook() {
    print "Enjoy your breakfast " + this.name;
  }
}

class Brunch < Breakfast {
  init(name, drink) {
    super.init(name);
    this.drink = drink;
  }
}

var johnBr = Breakfast("John");
johnBr.cook(); // "Enjoy your breakfast John"
```
