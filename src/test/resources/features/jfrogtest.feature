Feature: End-to-End JFrog Automation Flow

  #Scenario: Push a Docker image and perform security validations
    
    #Given Create a Docker repository with name "config.repo.name" or check if existing
    #Given I push the docker image "config.baseImage" to repo "config.repo.name" with name "config.customImage" and tag "config.tag"
    #And Ensure a new security policy "config.security_policy" is created or already exists
    #And A watch "config.getwatchname" is created for repo "config.repo" and policy "config.securitypolicy"
    #And I apply watch "config.watchnametoapply" from "config.watchstartdate" to "config.watchendtime"
    #And Verify the scan status for repo "config.repo" that was created
	#And Verify violations for watch and repo created above
	
	
Scenario: Validate the UI scenarios
	Given I open the JFrog login page
	Then I login to JFrog UI with username "config.username" and password "config.password"
	Then I should land on the JFrog dashboard
	Then I navigate to scan page for repository "config.repo"
	And I click on the docker image that was created by the API
	And I navigate to the Policy Violations tab
  