package com.outdatedversion.survival.persistence.service

import com.outdatedversion.survival.model.PointOfInterest
import java.util.*

interface PointsOfInterestService {
    fun save(owner: UUID, poi: PointOfInterest): PointOfInterest
    fun get(owner: UUID, id: UUID): PointOfInterest?
    fun getAll(owner: UUID): Array<PointOfInterest>
    fun remove(owner: UUID, poiId: UUID): PointOfInterest?
}