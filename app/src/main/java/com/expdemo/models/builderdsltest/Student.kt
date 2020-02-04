/*
 * Copyright 2020 Manish Patel. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expdemo.models.builderdsltest

/**
 * Created by Manish Patel on 2/4/2020.
 */
/** https://medium.com/mindorks/builder-pattern-vs-kotlin-dsl-c3ebaca6bc3b **/
class Student(
    val name: String?,
    val standard: Int,
    val rollNumber: Int
) {

    private constructor(builder: Builder) : this(builder.name, builder.standard, builder.rollNumber)

    class Builder {
        var name: String? = null
            private set
        var standard: Int = 0
            private set
        var rollNumber: Int = 0
            private set

        fun name(name: String) = apply { this.name = name }

        fun standard(standard: Int) = apply { this.standard = standard }

        fun rollNumber(rollno: Int) = apply { this.rollNumber = rollNumber }

        fun build() = Student(this)
    }
}

