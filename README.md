# Highlights homework
[Assignment](https://github.com/agrpdev/homeworks/blob/master/algorithms.md)

## Commands
* Build  `./gradlew clean build`
* Run `./gradlew bootRun`
* Requests
  * http://localhost:8080/highlights/<username>/documents/<elementId>/highlights  
    * headers  
  Content-Type: application/json
    * body  
      {
      "startElementId": "1",
      "offset": 20,
      "length": 10
      }

## Implementation
### Limitations
#### Whitespace handling
The assignment is not clear in the matter of whitespaces (inline vs. block formatting) The user should not be aware
of how many whitespaces, linebreaks or tabs are swallowed by the browser engine or if the text is inline or block
formatted but still expects the correct highlighting.
See [Mozilla whitespaces](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model/Whitespace)

Because of that, any containing whitespaces result in incorrect highlight placement.
In real world development this would cause discussion with PO regarding how to handle
this issue.
One of the solution would be to preprocess the html document prior storing it and leave
the formatting on css styles entirely