/*
 * PosixHelperFunctions.h
 *
 *       Created on:  Mar 10, 2015
 *  Last Updated on:  Feb 14, 2022
 *           Author:  Will Hedgecock
 *
 * Copyright (C) 2012-2022 Fazecast, Inc.
 *
 * This file is part of jSerialComm.
 *
 * jSerialComm is free software: you can redistribute it and/or modify
 * it under the terms of either the Apache Software License, version 2, or
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, version 3 or above.
 *
 * jSerialComm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of both the GNU Lesser General Public
 * License and the Apache Software License along with jSerialComm. If not,
 * see <http://www.gnu.org/licenses/> and <http://www.apache.org/licenses/>.
 */

#ifndef __POSIX_HELPER_FUNCTIONS_HEADER_H__
#define __POSIX_HELPER_FUNCTIONS_HEADER_H__

// Serial port JNI header file
#include <pthread.h>
#include "com_fazecast_jSerialComm_SerialPort.h"

// Serial port data structure
typedef struct serialPort
{
	pthread_mutex_t eventMutex;
	pthread_cond_t eventReceived;
	pthread_t eventsThread1, eventsThread2;
	char *portPath, *friendlyName, *portDescription, *portLocation, *readBuffer;
	int errorLineNumber, errorNumber, handle, readBufferLength, eventsMask, event;
	volatile char enumerated, eventListenerRunning, eventListenerUsesThreads;
} serialPort;

// Common storage functionality
typedef struct serialPortVector
{
	serialPort **ports;
	int length, capacity;
} serialPortVector;
serialPort* pushBack(serialPortVector* vector, const char* key, const char* friendlyName, const char* description, const char* location);
serialPort* fetchPort(serialPortVector* vector, const char* key);
void removePort(serialPortVector* vector, serialPort* port);

// Forced definitions
#ifndef CMSPAR
#define CMSPAR 010000000000
#endif
#ifndef O_CLOEXEC
#define O_CLOEXEC 0
#endif

// Linux-specific functionality
#if defined(__linux__)

typedef int baud_rate;
#ifdef __ANDROID__
extern int ioctl(int __fd, int __request, ...);
#else
extern int ioctl(int __fd, unsigned long int __request, ...);
#endif
void getDriverName(const char* directoryToSearch, char* friendlyName);
void getFriendlyName(const char* productFile, char* friendlyName);
void getInterfaceDescription(const char* interfaceFile, char* interfaceDescription);
void recursiveSearchForComPorts(serialPortVector* comPorts, const char* fullPathToSearch);
void driverBasedSearchForComPorts(serialPortVector* comPorts, const char* fullPathToDriver, const char* fullBasePathToPort);
void lastDitchSearchForComPorts(serialPortVector* comPorts);

// Solaris-specific functionality
#elif defined(__sun__)

#define faccessat(dirfd, pathname, mode, flags) access(pathname, mode)

#define LOCK_SH 1
#define LOCK_EX 2
#define LOCK_NB 4
#define LOCK_UN 8
typedef int baud_rate;
extern int ioctl(int __fd, int __request, ...);
int flock(int fd, int op);
void searchForComPorts(serialPortVector* comPorts);

// FreeBSD-specific functionality
#elif defined(__FreeBSD__) || defined(__OpenBSD__)

typedef int baud_rate;
void searchForComPorts(serialPortVector* comPorts);

// Apple-specific functionality
#elif defined(__APPLE__)

#define fdatasync fsync

#include <termios.h>
typedef speed_t baud_rate;
void searchForComPorts(serialPortVector* comPorts);

#endif

// Common Posix functionality
baud_rate getBaudRateCode(baud_rate baudRate);
int setBaudRateCustom(int portFD, baud_rate baudRate);
int verifyAndSetUserPortGroup(const char *portFile);

#endif		// #ifndef __POSIX_HELPER_FUNCTIONS_HEADER_H__
