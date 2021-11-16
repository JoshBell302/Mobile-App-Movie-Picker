# CS 492 Final Project 

Code and demo due by 5:00pm on Friday, 3/19/2021

In this course, a final programming project will take the place of formal exams to test your understanding of the material.  The final project will involve working with a team of 3-4 people to implement a substantial Android app that utilizes the major features we’ve looked at this term.  Specifically, you and your teammates will write an app that satisfies all of these requirements:

> It should have multiple activities the user can navigate between.

> It should use at least one implicit intent to launch another app.

> It should communicate via HTTP(s) with a third-party API to provide data for the app and optionally to send data back to the API.

> It must implement activity lifecycle methods and the ViewModel architecture to ensure that UI-related data is handled elegantly through lifecycle events.

> It should either store user preferences (via SharedPreferences or, if you’re feeling ambitious, the forthcoming DataStore library, now in alpha status) or store application data in device storage (using SQLite).  You may do both of these things if you want.

> It should have a polished, intuitive, and well-styled user interface.

> In addition, your app should implement at least one additional Android feature that isn’t covered in class.

> Some examples of possible features include: file storage, media playback, camera usage, etc.  You could also implement new navigation mechanisms or other architectural features that aren’t covered in class.  To get a better idea of the possibilities, it could be helpful to spend some time browsing the Android Jetpack documentation.

> Roughly, the remaining topics we’ll discuss in class are: the activity lifecycle and the ViewModel architecture; storing user preferences with SharedPreferences; storing structured data on device with SQLite; working with images and ViewPager navigation; background work and notifications.  These are things that won’t count for this additional feature requirement.

> Your app should be sufficiently different from the apps we write in class (i.e. the GitHub search app and the app from the OpenWeather API-based assignments) to demonstrate that your team understands and is able to use the tools and techniques covered in class.


You’ve written your proposals already, so you should know what app you’re going to work on.  This document contains a few more details about the process for the project. Link to this below
https://docs.google.com/document/u/1/d/1Fsqn03DP5qr3CTyQfJM9x6umIO4QMG98lH8MVxQncU0/edit?usp=sharing_eil&ts=6025b46d
# GitHub repositories

The code for your final project must be in a GitHub repository set up via GitHub Classroom.  You can use this link to form your team and create your final project repository:

https://classroom.github.com/g/_SNEhboY

The repository created for your team will be private by default, but you will have full administrative control over the repository that’s created for your project, which means you’ll be able to make it public if you wish.  I encourage you to make it public if you’re comfortable with it.  These final projects should be nice demonstrations of your Android development abilities and will be a good item to have in your CS portfolio.  It will be great to have the code in a public GitHub repo so you can share it easily when you want to.  


If you’ve already started a GitHub repo for your project, don’t worry.  The repository created via the GitHub classroom link above will be completely empty, so you can simply use git remotes to work with both repositories.  I can help you set that up if needed.

# Working with a team on a shared GitHub repo

When working with a team on a shared GitHub repo, it’s a good idea to use a workflow that uses branches and pull requests.  This has a few advantages:


By not working within the same branch, you can better avoid causing conflicts, which can occur when you and another member of your team edit the same parts of the code at the same time.

It helps you to be more familiar with the entire code base, even the parts that other team members are working on, because you’ll see all of the changes to the code as you review pull requests.  This can help you develop more rapidly because you won’t have to spend as much time understanding code that others have written.
It helps to ensure high quality code.  Code in pull requests is not incorporated into the master code branch until the code request is reviewed and approved.  That means everyone has a chance to improve pull request code before it becomes permanent.


One simple but effective branch- and pull-request-based workflow you might consider is the GitHub flow: https://guides.github.com/introduction/flow/.

# Grading demonstrations

The grade for your project will include a brief (10-15 minute) demonstration to me of your project’s functionality.  To get a grade for your project, your team must do a demo.  Demonstrations will be scheduled for finals week.  I’ll send more details on scheduling via email.
Code submission
All code for your final project must be pushed to the main branch of the repo created for your team using the GitHub Classroom link above before your grading demo.
Grading criteria
Your team’s grade (out of 100 points) will be computed based on the following criteria:
50 points – Your app satisfies the requirements listed on the first page of this document.
25 points – Your app has a high-quality design and implementation.
For example, your app is free of bugs and has an effective user interface.
25 points – Your app is creative and original.
If, for example, your app is simply a repackaging of the app we develop together during lecture or the one you developed during your assignments this term, you will likely not score highly in this category.

Remember, also, that if your team does not do a demo for your project, you will receive a zero for it.
Individual grades

Your individual grade for the project will be based on your team’s grade and also on evidence of your meaningful participation in your team’s work on the project, including from these sources:

# The commit log of your GitHub repository.
Your presence at and participation in your team’s project demo.
A team evaluation completed by each member of your project team.

In particular, if your GitHub commit log shows that you did not make meaningful contributions to your team’s implementation of your app, if you do not participate in your team’s demonstration of your app (without explicit prior approval by me), or if your project teammates submit team evaluations in which they agree that you did not do an appropriate share of the work on your final project, you will receive a lower grade on the project than your teammates.  I may use other sources as evidence of your participation, as well.
