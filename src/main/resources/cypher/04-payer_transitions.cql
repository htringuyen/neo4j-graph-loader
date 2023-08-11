    WITH row
    WITH row
    WHERE row.MEMBERID IS NOT NULL AND row.MEMBERID <> ''
    MATCH (patient:Patient {uuid:row.PATIENT}), (payer:Payer {uuid:row.PAYER})

    // create insurance with basic info
    WITH row, patient, payer
    CREATE (insr:Insurance {memberUuid:row.MEMBERID,
        planOwnership:row.PLAN_OWNERSHIP, ownerName:row.OWNER_NAME})
    CREATE (patient)-[:HAS_INSURANCE]->(insr)
    CREATE (insr)-[:FROM_PAYER]->(payer)

    // set start and end time of marker
    MERGE (startMk:Marker {uuid:row.PATIENT, value:datetime(row.START_DATE)})
    MERGE (insr)-[:INSURANCE_START]->(startMk)
    
    MERGE (endMk:Marker {uuid:row.PATIENT, value:datetime(row.END_DATE)})
    MERGE (insr)-[:INSURANCE_END]->(endMk)
