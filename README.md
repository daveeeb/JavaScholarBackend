# JavaScholarBackend | Academic Research & Data Analysis Tool

Server and Database Commands working with APIs

# Project Overview
JavaScholarBackend is a backend application designed to automate and structure academic research data by integrating with APIs like SerpApi (Google Scholar Engine).
It allows researchers to collect, store, and visualize information about authors, publications, and citations in a structured, queryable format.

The project follows the MVC (Model–View–Controller) architecture to maintain separation of concerns, improve scalability, and facilitate database integration.

# Project Purpose
The primary goal is to provide a robust and scalable tool that automates data collection from scholarly databases.
Instead of manually browsing through search results, this system extracts structured data about:

- Authors (name, email, institution, interests, etc.)
- Articles (title, authors, citations, links, etc.)
-  Research topics and citation metrics

# Key Functionalities
1. Targeted Academic Search
- Fetches author profiles and publications directly from Google Scholar using the SerpApi.
- Each request is authenticated via an API key stored in config.properties.

2. Data Structuring and Mapping
Raw JSON responses from the API are parsed and converted into model objects:
- Author
- Article
- Interest
These are then stored in a MySQL database for further use.

3. Database Integration
Uses a Data Access Object (DAO) pattern to store and retrieve authors and articles efficiently.
 Example:
 - ArticleDAO manages CRUD operations for articles.
 - Connection details are stored in a local configuration file.
 - Handles SQL exceptions, duplicate entries, and connection validation.

4. Error Handling & API Validation
- Includes a custom ApiException class for handling API response errors such as:
 - Invalid API key
 - Missing author or article data
 - HTTP response codes other than 200

# Project Relevance
This project solves the problem of manual, time-consuming academic data collection. It facilitates efficient, large-scale literature reviews and provides quantitative data that is often difficult or impossible to obtain directly from public interfaces.

# Database Setup (MySQL)
Create a database named scholar_db (or custom name) and include tables:
CREATE TABLE authors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  affiliations TEXT,
  email VARCHAR(255),
  thumbnail VARCHAR(255)
);

CREATE TABLE articles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(500),
  authors TEXT,
  publication VARCHAR(255),
  link VARCHAR(500),
  publication_date VARCHAR(255),
  cited_by INT
);

# Thecnical Document:
https://docs.google.com/document/d/1IPffeKb4wlQIUDWTSAUCSZAcVEEBBaBbOL2HOwQZHNU/edit?usp=sharing
 
