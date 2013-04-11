#import "CSGObjectsRegistry.h"

@interface CSGObjectsRegistry ()
{
}

@end

@implementation CSGObjectsRegistry

- (id) init
{
    if (self = [super init])
    {
    }
    return self;
}

- (CSGThread*) getThread: (CSGThread*) anInstance
{
    return anInstance;
}

- (CSGThreadInBlend*) getThreadInBlend: (CSGThreadInBlend*) anInstance
{
    return anInstance;
}

- (CSGThreadsBlend*) getThreadsBlend: (CSGThreadsBlend*) anInstance
{
    return anInstance;
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