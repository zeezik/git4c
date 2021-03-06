# 10. Quality Requirements



#### Merge Request template


```markdown

## What does this MR do?

(briefly describe what this MR is about)

## Checklist for the author

JIRA Ticket: ...

Should ticket be moved:
  - [ ] To Test
  - [ ] Done
  - [ ] This Pull Request is a part of bigger job
- Requirements are present in ticket:
  - [ ] done
  - [ ] not needed
- Solution Design is described in ticket:
  - [ ] done
  - [ ] not needed
- Specification is updated (BDD):
  - [ ] done
  - [ ] not needed
- Architecture Documentation updated:
  - [ ] done
  - [ ] not needed
- Development Guide updated: 
  - [ ] done
  - [ ] not needed
- Tester Guide updated:
  - [ ] done
  - [ ] not needed
- Installation Guide updated:
  - [ ] done
  - [ ] not needed
- Administration Guide updated:
  - [ ] done
  - [ ] not needed
- User Guide updated:
  - [ ] done
  - [ ] not needed
- Readme updated:
  - [ ] done
  - [ ] not needed
- Unit tests added / adapted (Infrastructure)
  - [ ] done
  - [ ] not needed
- Functional tests added / adapted (Use Case or Core)
  - [ ] done
  - [ ] not needed
- Integration tests added / adapted (API)
  - [ ] done
  - [ ] not needed
- UI tests added / adapted (Selenium)
  - [ ] done
  - [ ] not needed
- Selenium Tests works locally:
  - [ ] done
  - [ ] not needed
- Did you test manually on local environement:
  - [ ] done
  - [ ] not needed
- Team informed about breaking changes (altered DB schema, some new rules, way of building etc.)
  - [ ] done
  - [ ] no breaking changes this time
 What changes: ...
- In case of changes in DB schemas of the core modules, is there a migration script present?
  - [ ] done
  - [ ] no changes in core modules' schema were made  
 What changes: ...  
- [ ] Jira ticket moved to `Review`?  
- [ ] Potential reviewers informed about new MR  
- [ ] Branch merged with / rebased on current develop?  

**NOTE** If any of the points above is not finished, consider marking the MR with the WIP prefix

## Checklist for the reviewer

- [ ] CI pipeline passed- [ ] Check if actions marked as `not needed` are really not needed
- [ ] Changes made in the source code are OK  

## Checklist before merging by the author
- [ ] Source branch will be removed  
- [ ] At least 2 :thumbsup: earned  
- [ ] Jira ticket moved to `To Test` or to `Done`?  

## Checklist after merge (please fill it)
- [ ] If UI changes were made - relevant UI test passed  



```

