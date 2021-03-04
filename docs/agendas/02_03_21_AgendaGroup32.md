## Agenda Group 32

---

Date:           02-03-2021
Main focus:     Explaining what we implemented


# Points of action
* Discussed how we are going to store the different users, decided on abstaining from using abstract classes as storing in the database becomes way more complicated. Will instead use a String attribute in the User class containing the role of the user.
* Explained that we added dependencies for jersey so that we could create methods for HTTP requests.
* Explained the Requests class we created for creating HTTP requests.
* Explained how the HTTP requests relate to the endpoints and how they can be used to communicate with the server.
* Showed a demo of communication from client to server where we coupled the button on the mainScene that was provided in the demoproject to a post request posting a new question to the database. After posting an alert is created with the question that was returned from the server.
* Fixed a bug together where starting the server made it crash, it was related to a faulty method in the UserRepository and because multiple GET mappings were mapped to the same path.
* Explained how checkstyle works (guide on the bottom).
* Explained how FXML works and how it can be linked to the Java code to create (a very basic) GUI.

# Action points for next week (Scrum board)
* Starting GUI, will update on thursday


# Questions for the TA
* How should we transfer models onto the client?
* Feedback on issues on gitlab.
* Do we need to write tests using Mockito?
* Are we graded on the agendas?
* Should notes of meetings also be pushed?

# Checkstyle guide
1. In Intellij navigate to Gradle in the top-right corner
2. Go to Tasks > other
3. Running checkstyleMain will evaluate your code excluding tests. If there are any checkstyle violations on the clientside these will need to be resolved first. After this it will test for violations on the serverside. Running checkstyleTest will test your test-files on checkstyle

