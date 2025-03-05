import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

interface KeyResult {
  keyResultId: number;
  keyResultName: string;
  keyResultOwnerId: number;
  associatedObjectiveId: number;
  keyResultTargetVal: number;
  keyResultunit: string;
  keyResultPriority: string;
  keyResultDueDate: string;
  teamId: number;
}

interface Objective {
  objectiveId: number;
  objectiveName: string;
  mappedProject: number;
  keyResultIds: number[];
  objectiveCreatedAt: string;
  objectiveDueDate: string;
  objectiveStatus: string;
  objectivePriority: string;
  objectiveIsActive: boolean;
}

interface Team {
  teamId: number;
  teamName: string;
  teamLead: number;
  teamMembers: number[];
  assignedProject: number;
  assignedKeyResult: number[];
}

interface Project {
  projectId: number;
  projectName: string;
  projectDescription: string;
  projectPriority: string;
  projectStatus: string;
  projectManagerId: number;
  teamsInvolvedId: number[];
  objectiveId: number[];
  keyResultIds: number[];
  projectCreatedAt: string;
  projectDueDate: string;
  projectProgress: number;
  active: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AddProjectService {
  private currentProjectId = 1;
  private currentObjectiveId = 1;
  private currentKeyResultId = 1;

  private projectDataSubject = new BehaviorSubject<{
    project: Partial<Project>;
    teams: number[];
    objectives: Array<{
      objective: Partial<Objective>;
      keyResults: Partial<KeyResult>[];
    }>;
  }>({
    project: {},
    teams: [],
    objectives: []
  });

  projectData$ = this.projectDataSubject.asObservable();

  constructor() {}

  updateProjectInfo(projectInfo: any) {
    const currentData = this.projectDataSubject.value;
    const project: Partial<Project> = {
      projectId: this.currentProjectId,
      projectName: projectInfo.name,
      projectDescription: projectInfo.description || 'default_value',
      projectPriority: projectInfo.priority.toUpperCase(),
      projectStatus: 'ON_TRACK',
      projectManagerId: 1, // default_value
      projectDueDate: projectInfo.dueDate,
      projectCreatedAt: new Date().toISOString().split('T')[0],
      projectProgress: 0,
      active: true
    };

    this.projectDataSubject.next({
      ...currentData,
      project
    });
    console.log('[AddProjectService] Updated Project Info:', this.projectDataSubject.value);
  }

  updateTeamInfo(teamInfo: any) {
    const currentData = this.projectDataSubject.value;
    const teamsInvolvedId = teamInfo.teams || [];

    this.projectDataSubject.next({
      ...currentData,
      teams: teamsInvolvedId,
      project: {
        ...currentData.project,
        teamsInvolvedId
      }
    });
    console.log('[AddProjectService] Updated Team Info:', this.projectDataSubject.value);
  }

  updateObjectives(objectives: any[]) {
    const currentData = this.projectDataSubject.value;
    const processedObjectives = objectives.map((obj, index) => {
      const objectiveId = this.currentObjectiveId + index;
      const keyResultIds: number[] = [];
      
      const keyResults = obj.keyResults.map((kr: any, krIndex: number) => {
        const keyResultId = this.currentKeyResultId + krIndex;
        keyResultIds.push(keyResultId);
        
        return {
          keyResultId,
          keyResultName: kr.keyResultName,
          keyResultOwnerId: 1, // default_value
          associatedObjectiveId: objectiveId,
          keyResultTargetVal: kr.targetValue,
          keyResultunit: kr.unit,
          keyResultPriority: 'LOW', // default_value
          keyResultDueDate: 'default_value',
          teamId: currentData.teams[0] || 1 // Use first team or default
        };
      });

      return {
        objective: {
          objectiveId,
          objectiveName: obj.objectiveName,
          mappedProject: this.currentProjectId,
          keyResultIds,
          objectiveCreatedAt: new Date().toISOString().split('T')[0],
          objectiveDueDate: 'default_value',
          objectiveStatus: 'ON_TRACK',
          objectivePriority: 'LOW',
          objectiveIsActive: true
        },
        keyResults
      };
    });

    const allKeyResultIds = processedObjectives.flatMap(obj => 
      obj.keyResults.map((kr: Partial<KeyResult>) => kr.keyResultId)
    );

    const allObjectiveIds = processedObjectives.map(obj => 
      obj.objective.objectiveId
    );

    this.projectDataSubject.next({
      ...currentData,
      objectives: processedObjectives,
      project: {
        ...currentData.project,
        objectiveId: allObjectiveIds,
        keyResultIds: allKeyResultIds
      }
    });
    console.log('[AddProjectService] Updated Objectives:', this.projectDataSubject.value);
  }

  getFinalProjectData() {
    const data = this.projectDataSubject.value;
    const finalData = {
      project: data.project,
      objectives: data.objectives.map(obj => ({
        ...obj.objective,
        keyResults: obj.keyResults
      }))
    };
    
    // Return both object and formatted JSON string
    return {
      data: finalData,
      formattedJson: JSON.stringify(finalData, null, 2)
    };
  }

  reset() {
    this.projectDataSubject.next({
      project: {},
      teams: [],
      objectives: []
    });
  }
}
