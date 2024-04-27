package com.SafetyNet.SafetyNet.repository;

import com.SafetyNet.SafetyNet.model.FireStation;

public interface FireStationRepository {
    FireStation findByAddress(String address);
}
