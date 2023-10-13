/*
 * Copyright (C) 2016 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.koltinflowex.presentation.common.dexter;

import com.example.koltinflowex.presentation.common.dexter.listener.PermissionRequestErrorListener;
import com.example.koltinflowex.presentation.common.dexter.listener.multi.MultiplePermissionsListener;
import com.example.koltinflowex.presentation.common.dexter.listener.single.PermissionListener;

import java.util.Collection;

public interface DexterBuilder {

  DexterBuilder onSameThread();

  DexterBuilder withErrorListener(PermissionRequestErrorListener errorListener);

  void check();

  interface Permission {
    SinglePermissionListener withPermission(String permission);

    MultiPermissionListener withPermissions(String... permissions);

    MultiPermissionListener withPermissions(Collection<String> permissions);
  }

  interface SinglePermissionListener {
    DexterBuilder withListener(PermissionListener listener);
  }

  interface MultiPermissionListener {
    DexterBuilder withListener(MultiplePermissionsListener listener);
  }
}