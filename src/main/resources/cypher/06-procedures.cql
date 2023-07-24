  WITH row
  MATCH (p:Patient {id:row.PATIENT})
  MERGE (c:SNOMED_CT {code:row.CODE})
  SET c.description=row.DESCRIPTION, c:Procedure
  MERGE (cs:Encounter {id:row.ENCOUNTER, isEnd: false})
    ON CREATE
    SET cs.date=datetime(row.START)
  MERGE (p)-[:HAS_ENCOUNTER]->(cs)
  MERGE (cs)-[:HAS_PROCEDURE]->(c)
