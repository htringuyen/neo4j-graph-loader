WITH row
MATCH (p:Patient {id:row.PATIENT})
MERGE (c:SNOMED_CT {code:row.CODE})
SET c.description=row.DESCRIPTION, c:Condition
MERGE (cs:Encounter {id:row.ENCOUNTER, isEnd: false})
  ON CREATE
  SET cs.date=datetime(row.START)
MERGE (p)-[:HAS_ENCOUNTER]->(cs)
MERGE (cs)-[:HAS_CONDITION]->(c)
WITH p,c,cs,row
  WHERE row.STOP IS NOT NULL and row.STOP <> ''
MERGE (ce:Encounter {id:row.ENCOUNTER,
                     date:datetime(row.STOP)})
SET ce.isEnd=true
MERGE (p)-[:HAS_ENCOUNTER]->(ce)
MERGE (ce)-[:HAS_CONDITION]->(c)
MERGE (cs)-[:HAS_END]->(ce)
