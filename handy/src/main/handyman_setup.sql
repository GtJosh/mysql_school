CREATE DATABASE HANDYMAN

USE HANDYMAN

CREATE TABLE CUSTOMERS (  
  Email VARCHAR(50) NOT NULL UNIQUE,  
  Password VARCHAR(50) NOT NULL,  
  FirstName VARCHAR(50),  
  LastName VARCHAR(50),  
  HomePhone VARCHAR(15),  
  WorkPhone VARCHAR(15),  
  Address VARCHAR(50),  
  PRIMARY KEY (Email)  
)  

CREATE TABLE CLERKS (  
  ID SMALLINT UNSIGNED NOT NULL UNIQUE,  
  Password VARCHAR(50) NOT NULL,  
  EmpFirstName VARCHAR(50),  
  EmpLastName VARCHAR(50),  
  PRIMARY KEY (ID)  
)  

CREATE TABLE RESERVATIONS (  
  ReservationNumber MEDIUMINT UNSIGNED NOT NULL UNIQUE,  
  StartDate DATE NOT NULL,  
  EndDate DATE NOT NULL,  
  CreditCardNumber BIGINT UNSIGNED,  
  CreditCardExpiry DATE,  
  PickupClerk SMALLINT UNSIGNED,  
  DropoffClerk SMALLINT UNSIGNED,  
  CustomerEmail VARCHAR(50),  
  PRIMARY KEY (ReservationNumber),  
  FOREIGN KEY (PickupClerk) REFERENCES CLERKS (ID),  
  FOREIGN KEY (CustomerEmail) REFERENCES CUSTOMER(Email),    
  CHECK(StartDate < EndDate),  
  CHECK(CreditCardExpiry > EndDate)  
)  

CREATE TABLE TOOLS (  
  AbbrDescription VARCHAR(50),  
  TypeCategory ENUM('Hand Tools', 'Construction Equipment', 'Power Tools'),  
  ToolID SMALLINT UNSIGNED UNIQUE,  
  DepositCost SMALLINT UNSIGNED,  
  DailyRentalCost SMALLINT UNSIGNED,  
  PurchasePrice SMALLINT UNSIGNED,  
  FullDescription VARCHAR(250),  
  AvailableToRent BOOLEAN,  
  TotalServiceCost SMALLINT UNSIGNED,  
  SellCost SMALLINT UNSIGNED,  
  PRIMARY KEY (ToolID)  
)  

CREATE TABLE SERVICEREQUEST (  
  ToolID SMALLINT UNSIGNED NOT NULL UNIQUE,  
  StartDate DATE NOT NULL,  
  EndDate DATE NOT NULL,  
  EstimatedCost SMALLINT UNSIGNED,  
  PRIMARY KEY (StartDate),    
  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)  
)  

CREATE TABLE RESERVATION_TOOL (  
  ReservationNumber MEDIUMINT UNSIGNED NOT NULL,  
  ToolID SMALLINT UNSIGNED NOT NULL,  
  FOREIGN KEY (ReservationNumber) REFERENCES RESERVATIONS(ReservationNumber),  
  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)
)    

CREATE TABLE ACCESSORIES (  
  ToolID SMALLINT UNSIGNED NOT NULL,  
  Accessory VARCHAR(50) NOT NULL,  
  FOREIGN KEY (ToolID) REFERENCES TOOLS(ToolID)   
)
