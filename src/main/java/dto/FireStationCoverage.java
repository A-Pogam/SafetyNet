package dto;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.List;

public class FireStationCoverage {
    @JsonbProperty("coveredPeople")
    private List<CoveredPerson> coveredPeople;
    @JsonbProperty("adultsCount")
    private int adultsCount;
    @JsonbProperty("childrenCount")
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
