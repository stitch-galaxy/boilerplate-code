#import "CSGObjectsRegistry.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"
#import "CSGDesignCell.h"

#import "CSGMemorySetWithIndex.h"
#import "CSGMemorySet.h"


@interface CSGObjectsRegistry ()
{
}

@property (nonatomic, retain) CSGThread *tmpThread;
@property (nonatomic, retain) CSGMemorySet *threadsSet;

@property (nonatomic, retain) CSGThreadInBlend *tmpThreadInBlend;
@property (nonatomic, retain) CSGMemorySet *threadInBlendsSet;

@property (nonatomic, retain) CSGThreadsBlend *tmpThreadsBlend;
@property (nonatomic, retain) CSGMemorySet *threadBlendsSet;

@property (nonatomic, retain) CSGDesignCell *tmpCell;
@property (nonatomic, retain) CSGMemorySetWithIndex *cellsSet;

@end

@implementation CSGObjectsRegistry

@synthesize tmpThread;
@synthesize threadsSet;
@synthesize tmpThreadInBlend;
@synthesize threadInBlendsSet;
@synthesize tmpThreadsBlend;
@synthesize threadBlendsSet;
@synthesize tmpCell;
@synthesize cellsSet;

- (id) init
{
    return [self initWithThreadsMemorySetCapacity:1 ThreadsInBlendMemorySetCapacity:1 ThreadBlendsMemorySetCapacity:1 CellsMemorySetCapacity:1];
}

-(id) initWithThreadsMemorySetCapacity: (uint32_t) c1 ThreadsInBlendMemorySetCapacity: (uint32_t) c2 ThreadBlendsMemorySetCapacity: (uint32_t) c3 CellsMemorySetCapacity: (uint32_t) c4
{
    if (self = [super init])
    {
        tmpThread = [CSGThread alloc];
        threadsSet = [[CSGMemorySet alloc] initWithCapacity: c1];
        
        tmpThreadInBlend = [CSGThreadInBlend alloc];
        threadInBlendsSet = [[CSGMemorySet alloc] initWithCapacity:c2];
        
        tmpThreadsBlend = [CSGThreadsBlend alloc];
        threadBlendsSet = [[CSGMemorySet alloc] initWithCapacity:c3];
        
        tmpCell = [CSGDesignCell alloc];
        cellsSet = [[CSGMemorySetWithIndex alloc] initWithCapacity:c4];
        
    }
    return self;
}

- (CSGMemorySetWithIndex*) cellsMemorySet
{
    return cellsSet;
}

- (CSGMemorySet*) threadsMemorySet
{
    return threadsSet;
}

- (CSGMemorySet*) threadsInBlendMemorySet
{
    return threadInBlendsSet;
}

- (CSGMemorySet*) threadBlendsMemorySet
{
    return threadBlendsSet;
}

- (CSGDesignCell*) getDesignCellPrototype
{
    [tmpCell cleanup];
    return tmpCell;
}

- (CSGDesignCell*) getDesignCellByPrototype: (CSGDesignCell*) anPrototype
{
    CSGDesignCell* aCell = [cellsSet member:anPrototype];
    if (!aCell)
    {
        aCell = anPrototype.copy;
        [cellsSet putObject:aCell];
    }
    return aCell;
}

- (uint32_t) getDesignCellIndex: (CSGDesignCell*) aCell
{
    return [cellsSet getIndexByObject:aCell];
}

- (CSGDesignCell*) getDesignCellByIndex: (uint32_t) anIndex
{
    return [cellsSet getObjectByIndex:anIndex];
}


- (CSGThreadsBlend*) getThreadsBlendWithThreadsInBlend: (NSArray* ) threadsInBlend
{
    tmpThreadsBlend = [tmpThreadsBlend initWithThreadsInBlend:threadsInBlend];
    CSGThreadsBlend *aThreadsBlend = [threadBlendsSet member:tmpThreadsBlend];
    if (!aThreadsBlend)
    {
        aThreadsBlend = [[CSGThreadsBlend alloc] initWithThreadsInBlend:threadsInBlend];
        [threadBlendsSet putObject:aThreadsBlend];
    }
    return aThreadsBlend;
}

-(CSGThreadInBlend*) getThreadInBlendWithThread: (CSGThread*) aThread FlossCount: (uint8_t) aFlossCount
{
    tmpThreadInBlend = [tmpThreadInBlend initWithThread:aThread FlossCount:aFlossCount];
    CSGThreadInBlend *aThreadInBlend = [threadInBlendsSet member:tmpThreadInBlend];
    if (!aThreadInBlend)
    {
        aThreadInBlend = [[CSGThreadInBlend alloc] initWithThread:aThread FlossCount:aFlossCount];
        [threadInBlendsSet putObject:aThreadInBlend];
    }
    return aThreadInBlend;
}


- (CSGThread*) getThreadWithColor: (UIColor*) aColor
{
    tmpThread = [tmpThread initWithColor:aColor];
    CSGThread* aThread = [threadsSet member:tmpThread];
    if (!aThread)
    {
        aThread = [[CSGThread alloc] initWithColor:aColor];
        [threadsSet putObject:aThread];
    }
    return aThread;
}

- (CSGDesignPoint*) getDesignPoint: (CSGDesignPoint*) anInstance
{
    return anInstance;
}

- (CSGDesignPoints*) getDesignPoints: (CSGDesignPoints*) anInstance
{
    return anInstance;
}

- (CSGBackStitch*) getBackStitch: (CSGBackStitch*) anInstance
{
    return anInstance;
}

- (CSGStraightStitch*) getStraightStitch: (CSGStraightStitch*) anInstance
{
    return anInstance;
}

- (CSGDesign*) getDesign: (CSGDesign*) anInstance
{
    return anInstance;
}


@end