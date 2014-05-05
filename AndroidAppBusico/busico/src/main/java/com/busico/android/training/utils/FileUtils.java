/*
 * Copyright (C) 2008-2014  Marcelo Busico <marcelobusico@simbya.com.ar>
 *
 * This file is part of a SIMBYA project.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.busico.android.training.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void writeContentToFile(File outputFile, byte[] content) throws IOException {
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        bufferedOutputStream.write(content);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
}
