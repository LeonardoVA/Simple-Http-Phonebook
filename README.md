# Simple-Http-Phonebook

Installation instructions:

Download the jar & the textfile place textfile in home directory. 

If that is unkonwn load up the jar in console and it should print out your home directory for you.

Navigate in a browser to localhost:6060/help for usage or proceed to use one of the following commands.

Usage:

Localhost:6060/add/firstname/surname/number/address 

This will add a user to the database and sort it in alphabetical order.

Localhost:6060/search/searchterm

This will search the usernames for a match (partial or full) so searching "max" would return all entries with surnames which contain "max". 

Localhost:6060/list

This will list all entries in the phonebook database to view. (Including entry numbers which will be needed for the following commands)

Localhost:6060/remove/entrynumber

This will remove the entry from the database, you must find the entrynumber by listing entrys.

Localhost:6060/edit/entrynumber/firstname/surname/phonenumber/address(optional)

This will change the entrynumber specified, to the new values entered in the url.

After sufficient fun has been acchieved through usage you can stop the application by visiting: Localhost:6060/end

