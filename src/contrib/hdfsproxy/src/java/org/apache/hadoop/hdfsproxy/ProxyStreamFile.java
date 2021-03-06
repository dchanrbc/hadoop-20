/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfsproxy;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.server.namenode.StreamFile;
import org.apache.hadoop.security.UnixUserGroupInformation;
import org.apache.hadoop.conf.Configuration;

/** {@inheritDoc} */
public class ProxyStreamFile extends StreamFile {
  /** For java.io.Serializable */
  private static final long serialVersionUID = 1L;

  /** {@inheritDoc} */
  @Override
  protected DFSClient getDFSClient(HttpServletRequest request)
      throws IOException {
    ServletContext context = getServletContext();
    Configuration conf = new Configuration((Configuration) context
        .getAttribute("name.conf"));
    UnixUserGroupInformation.saveToConf(conf,
        UnixUserGroupInformation.UGI_PROPERTY_NAME, getUGI(request));
    InetSocketAddress nameNodeAddr = (InetSocketAddress) context
        .getAttribute("name.node.address");
    return new DFSClient(nameNodeAddr, conf);
  }

  /** {@inheritDoc} */
  @Override
  protected UnixUserGroupInformation getUGI(HttpServletRequest request) {
    return (UnixUserGroupInformation) request.getAttribute("authorized.ugi");
  }
}
