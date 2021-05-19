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

## Test requests
Example requests are stored in src/test/resources/testRequests.http
To get the document with generated highlights visibile to the user, add
** renderStyle=true** query parameter to the request

## Implementation

### Implementation notes
The process is divided into two phases:
* Storing new highlight by fitting it into existing highlight for particular user and document
* Rendering html document for the particular user

#### Document immutability
As I understood from the assignment, the document readable content cannot be changed, but the inline elements only. If
this is not the case, my code would not work and would require modifications

#### Span tag limitations
Span tag, according its definition, must not contain any block tags (as div is), so that any highlight spanned across
multiple divs needs to be split accordingly

#### Document sources
For the sake of simplicity, document CRUD is put aside, there are only two documents stored with the product with ids 1 and 2

#### Whitespaces
In the real world, the strong discussion would be hold how to handle whitespaces in HTML as what the user can see on th
page does not need to fit into how the html is represented on the background. See the [limitations](#whitespace-handling)

#### Integration tests
Omitted for the sake of simplicity (btw. that's why code coverage rate is not on desired level)

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