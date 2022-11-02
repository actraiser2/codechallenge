Feature: Searching by reference
	
	Scenario: Transaction not found
		Given A transaction that is not stored in our system
		When I check the status from any channel
		Then The system returns the status 'INVALID'
		
    Scenario: Case B
    	Given The transaction date is before today
    	Given A transaction that is stored in our system
		When I check the status from CLIENT channel
		Then The system returns the status 'SETTLED'
		Then the amount substracting the fee
		
	Scenario: Case C
		Given The transaction date is before today
		Given A transaction that is stored in our system
		When I check the status from INTERNAL channel
		Then The system returns the status 'SETTLED'
		Then the amount
		Then the fee
	
	Scenario: Case D
		Given The transaction date is equals to today
		And A transaction that is stored in our system
		When I check the status from ATM channel
		Then The system returns the status 'PENDING'
		And the amount substracting the fee
		
	Scenario: Case E
		Given The transaction date is equals to today
		And A transaction that is stored in our system
		When I check the status from INTERNAL channel
		Then The system returns the status 'PENDING'
		And the amount
		And the fee
		
 	
		