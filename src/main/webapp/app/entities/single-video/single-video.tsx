import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DurationFormat } from 'app/shared/DurationFormat';

import { getEntities } from './single-video.reducer';

export const SingleVideo = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const singleVideoList = useAppSelector(state => state.singleVideo.entities);
  const loading = useAppSelector(state => state.singleVideo.loading);
  const totalItems = useAppSelector(state => state.singleVideo.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="single-video-heading" data-cy="SingleVideoHeading">
        <Translate contentKey="monsterdamChicksApp.singleVideo.home.title">Single Videos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.singleVideo.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/single-video/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.singleVideo.home.createLabel">Create new Single Video</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {singleVideoList && singleVideoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('thumbnail')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.thumbnail">Thumbnail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnail')} />
                </th>
                <th className="hand" onClick={sort('thumbnailS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.thumbnailS3Key">Thumbnail S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnailS3Key')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('contentS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.contentS3Key">Content S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contentS3Key')} />
                </th>
                <th className="hand" onClick={sort('duration')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.duration">Duration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('duration')} />
                </th>
                <th className="hand" onClick={sort('likeCount')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.likeCount">Like Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.contentPackage">Content Package</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.singleVideo.creator">Creator</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {singleVideoList.map((singleVideo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/single-video/${singleVideo.id}`} color="link" size="sm">
                      {singleVideo.id}
                    </Button>
                  </td>
                  <td>
                    {singleVideo.thumbnail ? (
                      <div>
                        {singleVideo.thumbnailContentType ? (
                          <a onClick={openFile(singleVideo.thumbnailContentType, singleVideo.thumbnail)}>
                            <img
                              src={`data:${singleVideo.thumbnailContentType};base64,${singleVideo.thumbnail}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singleVideo.thumbnailContentType}, {byteSize(singleVideo.thumbnail)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singleVideo.thumbnailS3Key}</td>
                  <td>
                    {singleVideo.content ? (
                      <div>
                        {singleVideo.contentContentType ? (
                          <a onClick={openFile(singleVideo.contentContentType, singleVideo.content)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singleVideo.contentContentType}, {byteSize(singleVideo.content)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singleVideo.contentS3Key}</td>
                  <td>{singleVideo.duration ? <DurationFormat value={singleVideo.duration} /> : null}</td>
                  <td>{singleVideo.likeCount}</td>
                  <td>
                    {singleVideo.createdDate ? <TextFormat type="date" value={singleVideo.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {singleVideo.lastModifiedDate ? (
                      <TextFormat type="date" value={singleVideo.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{singleVideo.createdBy}</td>
                  <td>{singleVideo.lastModifiedBy}</td>
                  <td>
                    {singleVideo.deletedDate ? <TextFormat type="date" value={singleVideo.deletedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {singleVideo.contentPackage ? (
                      <Link to={`/content-package/${singleVideo.contentPackage.id}`}>{singleVideo.contentPackage.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{singleVideo.creator ? <Link to={`/user-lite/${singleVideo.creator.id}`}>{singleVideo.creator.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/single-video/${singleVideo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/single-video/${singleVideo.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/single-video/${singleVideo.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.singleVideo.home.notFound">No Single Videos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={singleVideoList && singleVideoList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default SingleVideo;
