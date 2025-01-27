# CalorieTracker

Few thing to keep in mind:

- When pulling the code, don't forget to change the IP address in VolleyRequestContainer class to the IP of your machine. 
- Also, make sure that the code on lines 43 to 54 in LoginActivity are UNCOMMENTED and that the lines 39 to 41 are COMMENTED.
    - The lines were commented out for dev purposes.

Cloning the Repository:

- In the Fork application, got to File > Clone.
- Copy the http url from github
- Paste the url into fork where it asks for Repository URL.
- Choose a parent directory where you want the project to be located.
- Press Clone.

Creating a New Branch:

- In the Fork Application, make sure you are in the main branch. (You will see the branch name at the center|top of the fork application below the repository name.
- Next to the repo name, there is a button that creates a new branch, press it and name the branch "task/issue#/Title_what_you_are_doing".
- Make sure the checkbox "check out after create" is checked.
- Press the button "Create and Checkout".
- Now you arre in your new branch (You can see the branch name at the center|top of the application below he repo name).

Fetching

- Fetching will update the tree, which will display any new information the the "tree" of commits.
- The Fetch button is on the top bar. It's icon is a "hollow DOWN arrow".

Pulling

- The Pulling button is on the top bar. It's icon is a "solid DOWN arrow".
- Pulling will update your code.
- To properly pull from the master branch... Press the pull icon.
- Next select the Remote branch as "origin".
- Select the branch as the name of your current branch that you created (or are in).
- Make sure Both checks boxs are checked ("Rebase instead of merge" and "Stash and reapply local changes")
- Press "Pull"

After Completing a task (Coding):

- In fork, on the side bar, there is an item labeled "Changes (#)". Click it.
- These are all the changes that were done to the code.
- Make sure everything is good to go, then Select all the files (CTRL/CMD A).
- All Files should be highlighted, then press the button "stage".
- All files will "jump" to the bottom box.
- Next, write a commit message explaining what you did (One liner).
- Then press "Commit".
- The files are now commited, but they will not appear in github yet. You will need to push the branch...

Pushing

- The Pushing button is on the top bar. It's icon is a "solid UP arrow".
- Pushing will push all the code that you commited to your branch to github.
- When pushing code, MAKE SURE YOU ARE PUSHING TO YOUR BRANCH AND NOT TO THE MAIN (MASTER) BRANCH
- After pressing the push icon, select Branch: as your current branch (default usually)
- To: should be origin/branch_that_you_are_in
- Check "Push all tags".
- Press the Push Button.

Submitting a Pull Request:

- After pushing your code to github, you have to make a pull request so that the rest of the team can review your changes.
- Go to Github > Then go to your repository the press the "code" tab.
- There should be a green box Saying to make a "Pull Request".
- Press it and you can write a message if you want.
- Then Submit the pull request.
- The team will review the pull request and give feedback if necessary.
- When everything is good to go, someone in the team can merge the pull request to the master branch.

- After the Code has been merged to master branch, do a pull and rebase in the fork application do update your local branch with the code in master.
