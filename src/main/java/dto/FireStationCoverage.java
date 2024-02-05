package dto;

import java.util.List;

public class FireStationCoverage {
    private List<CoveredPerson> coveredPeople;
    private int adultsCount;
    private int childrenCount;


    public void setCoveredPeople(List<CoveredPerson> coveredPeople) {
        this.coveredPeople = coveredPeople;
    }

    public void setAdultsCount(int adultsCount) {
        this.adultsCount = adultsCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

}
