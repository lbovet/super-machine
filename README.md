# super-machine
[![Build Status](https://travis-ci.org/lbovet/super-machine.svg?branch=master)](https://travis-ci.org/lbovet/super-machine)
<p align='right'>A <a href="http://www.swisspush.org">swisspush</a> project <a href="http://www.swisspush.org" border=0><img align="top"  src='https://1.gravatar.com/avatar/cf7292487846085732baf808def5685a?s=32'></a></p>

Query Java object graphs in a typed and streamed fashion

<p align='center'><img src='https://cloud.githubusercontent.com/assets/692124/26805208/2e97dd88-4a4b-11e7-8060-7c140963363d.png' /></p>

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

`find` traverses the object graph to find all occurences in properties, maps and collections. In the example above, the structure could be:

```
Invoice
   |
   | *
 Lines  --- Article
               | *
               |
             Vendor
               | *
               |
            Company
               |
               | *
             Office --- Staff --- * Employee --- Person

```

Nice?

