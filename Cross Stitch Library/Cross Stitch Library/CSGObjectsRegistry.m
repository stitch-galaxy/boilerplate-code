#import "CSGObjectsRegistry.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"
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


@end

@implementation CSGObjectsRegistry

@synthesize tmpThread;
@synthesize threadsSet;
@synthesize tmpThreadInBlend;
@synthesize threadInBlendsSet;
@synthesize tmpThreadsBlend;
@synthesize threadBlendsSet;

- (id) init
{
    if (self = [super init])
    {
        tmpThread = [CSGThread alloc];
        threadsSet = [[CSGMemorySet alloc] init];
        
        tmpThreadInBlend = [CSGThreadInBlend alloc];
        threadInBlendsSet = [[CSGMemorySet alloc] init];
        
        tmpThreadsBlend = [CSGThreadsBlend alloc];
        threadBlendsSet = [[CSGMemorySet alloc] init];
    }
    return self;
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

- (CSGStitchInCell*) getStitchInCell: (CSGStitchInCell*) anInstance
{
    return anInstance;
}

- (CSGDesignCell*) getDesignCell: (CSGDesignCell*) anInstance
{
    return anInstance;
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