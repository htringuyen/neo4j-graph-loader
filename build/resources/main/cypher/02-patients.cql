    WITH row
    // set basic info
    MERGE (p:Patient {uuid:row.Id})
        ON CREATE
        SET p.ssn = row.SSN,
            p.firstName = row.FIRST,
            p.lastName = row.LAST,
            p.healthcareExpenses = toFloat(row.HEALTHCARE_EXPENSES),
            p.healthcareCoverage = toFloat(row.HEALTHCARE_COVERAGE),
            p.income = toFloat(row.INCOME)
    
    // set birthdate
    WITH row, p
    MERGE (birthMk:Marker {uuid:p.uuid, value:datetime(row.BIRTHDATE)})
    MERGE (p)-[:BIRTHED]->(birthMk)

    // set deathdate
    WITH row, p
    FOREACH(ignore IN CASE WHEN row.DEATHDATE IS NOT NULL AND row.DEATHDATE <> '' THEN [1] ELSE [] END |
        MERGE (deathMk:Marker {uuid:p.uuid, value:datetime(row.DEATHDATE)})
        MERGE (p)-[:DIED]->(deathMk)
    )

    // set sex
    WITH row, p, CASE WHEN row.GENDER = "F" THEN ["FEMALE"] ELSE ["MALE"] END AS sex
    MERGE (gender:Gender {alpha:row.GENDER})
        ON CREATE
        SET gender.sex = sex
    MERGE (p)-[:HAS_GENDER]->(gender)

    // set marital state
    WITH row, p,
        CASE WHEN row.MARITAL IS NOT NULL AND row.MARITAL <> '' THEN row.MARITAL ELSE "U" END AS mAlpha,
        CASE row.MARITAL
        WHEN "M" THEN "MARRIED"
        WHEN "S" THEN "SINGLE"
        WHEN "D" THEN "DIVORCE"
        WHEN "W" THEN "WIDOWING"
        ELSE "UNKNOW" END
        AS mType
    MERGE (maritalStatus:MaritalStatus {alpha:mAlpha})
        ON CREATE
        SET maritalStatus.type = mType  
    MERGE (p)-[:HAS_MARITAL_STATUS]->(maritalStatus)

    // set race
    WITH row, p
    MERGE (race:Race {alphaCode:toUpper(row.RACE)})
    MERGE (p)-[:HAS_RACE]->(race)

    // set ethnicity
    WITH row, p
    MERGE (ethnicity:Ethnicity {alphaCode:toUpper(row.ETHNICITY)})
    MERGE (p)-[:HAS_ETHNICITY]->(ethnicity)

    // set address
    WITH row, p
    MATCH (state:State {name:row.STATE})
    MATCH (county:County)-[:STATE]->(state)
    WHERE county.fips = row.FIPS OR county.name = row.COUNTY

    WITH row, p, county
    MATCH (county)<-[:COUNTY]-(city:City)
    WHERE toLower(trim(city.name)) = toLower(trim(row.CITY))
    
    CREATE (address:Address {streetAddress:row.ADDRESS})
    CREATE (address)-[:CITY]->(city)
    CREATE (p)-[:HAS_ADDRESS]->(address)

