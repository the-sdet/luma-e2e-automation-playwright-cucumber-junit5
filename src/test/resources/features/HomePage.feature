#Author: Pabitra Swain (pabitra.swain.work@gmail.com)
@homepage
Feature: Test Home Page of Luma Store

  Scenario: Home Page Components
    Then Logo is displayed
    Then SearchBox is displayed
    Then Cart Icon is displayed
    Then Menu Bar is displayed

  Scenario: Menu Bar Options
    Then Menu Has These Options
      | What's New |
      | Women      |
      | Men        |
      | Gear       |
      | Training   |
      | Sale       |
    Then "Women" Menu Item has child menu options as below
      | Tops -> Jackets, Hoodies & Sweatshirts, Tees, Bras & Tanks |
      | Bottoms -> Pants, Shorts                                   |
    Then "Men" Menu Item has child menu options as below
      | Tops -> Jackets, Hoodies & Sweatshirts, Tees, Tanks |
      | Bottoms -> Pants, Shorts                            |
    Then "Gear" Menu Item has child menu options as below
      | Bags              |
      | Fitness Equipment |
      | Watches           |
    Then "Training" Menu Item has child menu options as below
      | Video Download |

  Scenario: Hot Sellers Sections and Products
    Then Hot Sellers Section is displayed
    And Hot Seller Items are displayed
      | Radiant Tee -> $22.00            |
      | Breathe-Easy Tank -> $34.00      |
      | Argus All-Weather Tank -> $22.00 |
      | Hero Hoodie -> $54.00            |
      | Fusion Backpack -> $59.00        |
      | Push It Messenger Bag -> $45.00  |