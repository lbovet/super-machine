# super-machine
Query Java object graphs in a typed and streamed fashion

What about:

```java
from(invoice).
  .find(Article.class)
  .filter(article -> article.getType().equals("hardware")
  .extract(Article::getVendor)
  .filter(vendor -> !vendor.getName().equals("Apple")
  .find(Office.class)
  .then( 
    (offices -> offices.extract(Office::getCity)),
    (offices -> offices.find(Person.class).extract(Person::getFullName)))
  .stream()
```

_Returns the name of employees and city name of the offices of non-Apple vendors that sells hardware article on this invoice._

Nice?

