# WordsHunter

### Functionality
A client for the examination service. All the data is on the backend's side, this android app just fetches it dynamically and gives the following opportunities (for user is a student):
- Get a list of the available exams
- Pass an available exam
- Get a list of the passed exams
- Get results of a passed exam
- Get information about the account
### Learnt and used
This app features the use of the following libs:
- Retrofit
- Dagger (2)
- Butterknife
- Otto bus (now deprecated)

Since it was my first big project, the main difficulty (and the main value for the learning purpose) was to figure out how to arrange and combine all those minor modules and instruments to a major system.
And since this is just a client, this whole app is built on the communication with the server, so i had to think it over too.