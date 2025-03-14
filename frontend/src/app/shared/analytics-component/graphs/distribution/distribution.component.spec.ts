import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DistributionComponent } from './distribution.component';
import { FormsModule } from '@angular/forms';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common';

describe('DistributionComponent', () => {
  let component: DistributionComponent;
  let fixture: ComponentFixture<DistributionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DistributionComponent, FormsModule, NgxChartsModule, CommonModule]
    }).compileComponents();

    fixture = TestBed.createComponent(DistributionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have initial key result selection', () => {
    expect(component.selectedKey).toBe('main');
  });

  it('should have correct data structure', () => {
    expect(component.pieChartData.length).toBe(3);
    expect(component.pieChartData[0].name).toBe('On Track');
    expect(component.pieChartData[1].name).toBe('At Risk');
    expect(component.pieChartData[2].name).toBe('Completed');
  });

  it('should toggle dropdown visibility', () => {
    expect(component.showDropdown).toBeFalse();
    component.toggleDropdown();
    expect(component.showDropdown).toBeTrue();
    component.toggleDropdown();
    expect(component.showDropdown).toBeFalse();
  });

  it('should update pie chart data when selecting key results', () => {
    component.selectKeyResult('kr1');
    expect(component.pieChartData).toEqual([
      { name: 'On Track', value: 60 },
      { name: 'At Risk', value: 25 },
      { name: 'Completed', value: 15 }
    ]);

    component.selectKeyResult('kr2');
    expect(component.pieChartData).toEqual([
      { name: 'On Track', value: 40 },
      { name: 'At Risk', value: 35 },
      { name: 'Completed', value: 25 }
    ]);
  });

  it('should return correct key result name', () => {
    expect(component.getSelectedKeyResultName()).toBe('Objective Progress');
    component.selectKeyResult('kr1');
    expect(component.getSelectedKeyResultName()).toBe('Increase Revenue by 30%');
  });
});
