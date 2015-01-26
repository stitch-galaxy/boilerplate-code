#pragma once

#include <deque>
#include "SingleLock.h"
#include "CriticalSection.h"

template <class T>
class CSafeQueue
{
	std::deque<T> m_queue;
	ThreadWrappers::CCriticalSection m_lock;
public:
	void append(const T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		m_queue.push_back(item);
	}

	void insert(const T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		m_queue.push_front(item);
	}

	bool get(T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		if(m_queue.size() == 0)
			return false;
		item = m_queue.front();
		m_queue.pop_front();
		return true;
	}
	size_t size() const
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		return m_queue.size();
	}
};
/*
template <class T>
class CSafeVector
{
	std::vector<T> m_vector;
	ThreadWrappers::CCriticalSection m_lock;
public:
	void add(const T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		m_vector.push_back(item);
	}
	
	void insert(const T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		m_vector.insert(m_vector.begin(), item);
	}

	bool get(T& item)
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		if(m_vector.size() == 0)
			return false;
		item = *m_vector.begin();
		m_vector.erase(m_vector.begin());
		return true;
	}
	
	size_t size() const
	{
		ThreadWrappers::CSingleLock<ThreadWrappers::CCriticalSection> locker(m_lock);
		return m_vector.size();
	}
};
*/