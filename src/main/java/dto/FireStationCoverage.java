package dto;

import java.util.List;

public class FireStationCoverage {
    private List<CoveredPerson> coveredPeople;
    private int adultsCount;
    private int childrenCount;

    public void setCoveredPeople(List<CoveredPerson> coveredPeople) {
        this.coveredPeople = coveredPeople;
    }

    public List<CoveredPerson> getCoveragePeople() {
        return coveredPeople;
    }

    public void setAdultsCount(int adultsCount) {
        this.adultsCount = adultsCount;
    }

    public int getAdultCount() {
        return adultsCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }
}
