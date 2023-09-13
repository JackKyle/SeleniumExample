# language: en
@Finance @Budgets
Feature: Budgets: Reporting

  @MANUAL @OPTSPENDQA-509 
  Scenario Outline: Budgets: More: Auto Create New Budget
    Given I am logged in as system admin
    When I navigate to Budgets Module
    And I select: "MasterBillingCenter" in filterBy: "Billing Center"
    And I select viewBy filter: "Vendor"
    And I enter: "<Vendor>" in filterBy: "Vendor"
    And I click "report" dropdown button
    And I select: "<Currency>" on currency dropdown on budgets report
    And I enable Use Saved Allocations checkbox
    And I click: "Generate" button
    And I wait "3" seconds
    Then Budget Report Monthly dialog is displayed

    Examples: 
      | Vendor        | Format  | Currency    |
      | Master_Vendor | PREVIEW | U.S. Dollar |
