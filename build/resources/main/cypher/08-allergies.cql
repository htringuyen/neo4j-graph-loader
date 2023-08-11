    WITH row
    MATCH (enc:Encounter {uuid:row.ENCOUNTER})
    WITH row, enc
    CREATE (enc)-[:HAS_ALLERGY]->(alrg:Allergy)
    WITH row, alrg
    CALL apoc.create.setLabels(alrg, ["Allergy", toUpper(row.CATEGORY), toUpper(row.TYPE)]) YIELD node

    // set start and end time
    WITH row, alrg
    MERGE (startMk:Marker {uuid:row.PATIENT, value:datetime(row.START)})
    CREATE (alrg)-[:ALLERGY_STARTED]->(startMk)
    FOREACH(ignore IN CASE WHEN row.STOP IS NOT NULL AND row.STOP <> '' THEN [1] ELSE [] END |
        MERGE(endMk:Marker {uuid:row.PATIENT, value:datetime(row.STOP)})
        CREATE (alrg)-[:ALLERGY_ENDED]->(endMk)
    )

    // set snomed allergy
    WITH row, alrg
    MERGE (alrgCode:SNOMED_CT {identifier:row.CODE})
        ON CREATE
        SET alrgCode.description = row.DESCRIPTION
    CREATE (alrg)-[:CODED_AS]->(alrgCode)

    // set snomed reactions
    WITh row, alrg, [row.REACTION1, row.REACTION2] AS recCodes, [row.DESCRIPTION1, row.DESCRIPTION2] AS recDescriptions,
        [row.SEVERITY1, row.SEVERITY2] AS recSeverities
    UNWIND [0, 1] AS i
    WITH alrg, recCodes[i] AS recCode, recDescriptions[i] AS recDesr, toUpper(recSeverities[i]) AS recSeverity
    WHERE recCode IS NOT NULL AND recCode <> ''
    MERGE (code:SNOMED_CT {identifier: recCode})
        ON CREATE
        SET code.description = recDesr
    WITH alrg, code, recSeverity
    CALL apoc.create.relationship(alrg, "HAS_REACTION", {severity:recSeverity}, code) YIELD rel
    RETURN alrg


