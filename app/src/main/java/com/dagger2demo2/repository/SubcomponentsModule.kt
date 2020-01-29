package com.dagger2demo2.repository

import dagger.Module

/**
 * Created by Manish Patel on 1/29/2020.
 */
// The "subcomponents" attribute in the @Module annotation tells Dagger what
// Subcomponents are children of the Component this module is included in.
@Module(subcomponents = [LoginComponent::class])
class SubcomponentsModule {
}